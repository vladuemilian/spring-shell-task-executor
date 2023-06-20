package io.packstrap.taskexecutor.module.transaction;

import io.packstrap.taskexecutor.module.transaction.dto.TransactionDto;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Singleton component used to keep track of what was the last file processed for a transaction that was read.
 */
@Component
@Data
public class TransactionContext {
    private TransactionDto lastTransactionDto;

    public boolean exists() {
        return this.lastTransactionDto != null;
    }
}
