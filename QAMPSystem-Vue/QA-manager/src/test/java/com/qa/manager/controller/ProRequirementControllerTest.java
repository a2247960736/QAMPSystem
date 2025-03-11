package com.qa.manager.controller;

import com.qa.manager.domain.ProRequirement;
import com.qa.manager.domain.ProRequirementVo;
import com.qa.manager.service.IProRequirementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 22479
 * Date: 2025-03-10
 * Time: 14:00
 */
class ProRequirementControllerTest {

    @Resource
    private IProRequirementService proRequirementService;

    @Test
    void list() {
        ProRequirement  proRequirement = new ProRequirement();
        proRequirement.setProjectId(3L);
        List<ProRequirementVo> list = proRequirementService.selectProRequirementVoList(proRequirement);
        for (ProRequirementVo proRequirementVo : list) {
            System.out.println(proRequirementVo);
        }

    }
}