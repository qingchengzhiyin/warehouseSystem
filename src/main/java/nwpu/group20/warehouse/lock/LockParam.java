package nwpu.group20.warehouse.lock;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

public class LockParam {
    private static long HOLD_LOCK_TIME = 300L;//占据锁的时间
    private static long TRY_LOCK_TIME = 1000L;//最大尝试重新获取锁的时间

    private String lockKey;
    private String lockValue;
    private long holdLockTime;
    private long tryLockTime;

    public LockParam(String lockKey,long holdLockTime,long tryLockTime){
        this.lockKey = lockKey;
        this.holdLockTime = holdLockTime;
        this.tryLockTime = tryLockTime;
    }

    public  LockParam(String lockKey){
        this(lockKey,HOLD_LOCK_TIME,TRY_LOCK_TIME);
    }
}
