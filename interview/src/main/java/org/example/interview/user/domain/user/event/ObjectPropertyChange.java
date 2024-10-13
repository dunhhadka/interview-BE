package org.example.interview.user.domain.user.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Triple;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ObjectPropertyChange {
    private String propertyName;
    private Object oldValue;
    private Object currentValue;

    public ObjectPropertyChange(Triple<String, Object, Object> diff) {
        this.propertyName = diff.getLeft();
        this.oldValue = diff.getMiddle();
        this.currentValue = diff.getRight();
    }
}
