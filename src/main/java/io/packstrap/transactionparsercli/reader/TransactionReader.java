package io.packstrap.transactionparsercli.reader;

import io.packstrap.transactionparsercli.Discoverable;
import io.packstrap.transactionparsercli.dto.TransactionDto;

public interface TransactionReader extends Discoverable {
    /**
     * Method used to perform the actual logic of reading, based on the received inputs.
     * @param input
     * @return
     */
    TransactionDto read(Object input);
}
