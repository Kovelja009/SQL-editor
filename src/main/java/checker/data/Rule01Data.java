package checker.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import resources.implementation.Entity;

import java.util.List;

@Getter
@AllArgsConstructor
public class Rule01Data {
    List<String> wrongColumns;
    Entity entity;

}
