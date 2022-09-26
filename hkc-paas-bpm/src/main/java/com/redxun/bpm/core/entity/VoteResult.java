package com.redxun.bpm.core.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 投票结果。
 * <pre>
 *     1.投票是否完成
 *     2.通过状态。（通过还是拒绝)
 * </pre>
 */
@Getter
@Setter
public class VoteResult{

    public VoteResult(){
    }

    public VoteResult(Boolean completed,  Boolean result){
        this.completed=completed;
        this.result=result;
    }

    /**
     * 投票是否完成
     */
    private Boolean completed=false;
    /**
     * 通过状态。（通过还是拒绝)
     */
    private Boolean result=false;
}
