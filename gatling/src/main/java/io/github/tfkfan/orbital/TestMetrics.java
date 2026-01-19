package io.github.tfkfan.orbital;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestMetrics {
    private long startTimeMs;
    private int playersCountAtStart;
    private long endTimeMs;
    private int playersCountAtEnd;

    public Long endAbsoluteMs(){
        return endTimeMs - startTimeMs;
    }
}
