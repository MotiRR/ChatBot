package sber.edu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sber.edu.database.dto.ResultAnalysisEntity;
import sber.edu.services.ResultAnalysisService;

import java.util.List;

@RestController
public class ResultAnalysisRest {

    @Autowired
    ResultAnalysisService resultAnalysis;

    @GetMapping("/administration/allSentencesAndWordsUser")
    @ResponseBody
    public List<ResultAnalysisEntity> allSentencesAndWordsUser(@RequestParam(name = "userId")
                                                                           Integer userId) {
        return resultAnalysis.allSentencesAndWordsUser(userId);
    }
}
