package org.example.service;

import org.springframework.stereotype.Service;
import org.teamview.model.Team;
import org.teamview.repository.DynamoBuilder;

import java.util.List;

@Service
public class TeamService {


    public List<Team> getTeams() {

        DynamoBuilder repository = DynamoBuilder.createBuilder();

        List<Team> teams = repository.getAllTeams();


        // todo: dto ...
        return teams;
    }
}
