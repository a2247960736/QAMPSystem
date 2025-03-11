package com.qa.manager.domain.Vo;

import com.qa.manager.domain.ProjectMember;
import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 22479
 * Date: 2025-03-11
 * Time: 20:53
 */
@Data
public class ProjectMemberDTO extends ProjectMember {
    private List<Long> userIds;
}
