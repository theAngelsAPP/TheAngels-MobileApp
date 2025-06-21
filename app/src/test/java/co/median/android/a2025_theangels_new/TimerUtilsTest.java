package co.median.android.a2025_theangels_new;

import org.junit.Test;
import static org.junit.Assert.*;

import co.median.android.a2025_theangels_new.util.TimerUtils;

public class TimerUtilsTest {
    @Test
    public void formatDuration_returnsCorrectFormat() {
        assertEquals("00:00", TimerUtils.formatDuration(0));
        assertEquals("00:05", TimerUtils.formatDuration(5));
        assertEquals("01:05", TimerUtils.formatDuration(65));
    }
}
