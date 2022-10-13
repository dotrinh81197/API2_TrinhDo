package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.issue.Fields;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Issue {
    private String id;
    private String self;
    private String key;
    private Fields fields;
}
