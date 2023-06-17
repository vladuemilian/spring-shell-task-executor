package io.packstrap.transactionparsercli.writer;

import io.packstrap.transactionparsercli.Discoverable;

public interface TransactionWriter extends Discoverable {
    boolean write(Object input);
}
