package io.github.tfkfan.orbital.core.model;

import java.io.Serializable;

public interface Entity<I extends Serializable> {
    I getId();
}
