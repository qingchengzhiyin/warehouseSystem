package nwpu.group20.warehouse.lock;

import redis.clients.jedis.Jedis;

import java.util.Collections;

/**
 *redis分布锁,初始化要传入一个LockParam,使用方法要传入id
 * LockParam要传入键，也就是任务类型，还有占据时间和重试时间，有默认值可以不写
 */
public class RedisLock {
    private long tryLockEndTime;//终止获取锁的时间
    private String lockKey;
    private String lockValue;
    private final String LOCK_SUCESS = "OK";//redis的set成功后返回ok
    private final Long UNLOCK_SUCESS = 1L;//redis的delete成功后返回1
    private final String passwd = "password";
    private LockParam lockParam;
    private Jedis jedis;
    private final String releaseScript =
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                    "return redis.call('del', KEYS[1]) " +
                    "else " +
                    "return 0 " +
                    "end";

    public RedisLock(LockParam lockParam){
        if(lockParam == null){
            throw new RuntimeException("LockParam is null");
        }
        this.lockKey = lockParam.getLockKey();
        this.tryLockEndTime = System.currentTimeMillis() + lockParam.getTryLockTime();
        this.jedis = new Jedis("localhost",6379);
        this.jedis.auth(passwd);
        this.lockParam = lockParam;
    }

    public void close(){
        this.jedis.close();
    }

    public Boolean tryLock(int userId){
        String flag;
        flag = jedis.set(lockKey, String.valueOf(userId),"NX","EX",lockParam.getHoldLockTime());
        if(LOCK_SUCESS.equals(flag)){
            return true;
        }
        return false;
    }

    public Boolean lock(int userId){
        while (true){
            if(System.currentTimeMillis() > tryLockEndTime){
                return false;//超过获取锁的最终时间
            }
            if(tryLock(userId)){
                return true;
            }else {
                try {
                    Thread.sleep(50L);//等待50秒重试
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public Boolean unLock(int userId){
        Object result;

        try{
            result = jedis.eval(releaseScript, Collections.singletonList(lockKey), Collections.singletonList(String.valueOf(userId)));
            if(UNLOCK_SUCESS.equals(result)){
                return true;
            }
        }finally {
            close();
        }
        return false;
    }



}
