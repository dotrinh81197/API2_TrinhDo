package model.issue;

import lombok.Data;

@Data
public class Fields {
    private String summary;
    private IssueType issuetype;
    private Project project;
}
