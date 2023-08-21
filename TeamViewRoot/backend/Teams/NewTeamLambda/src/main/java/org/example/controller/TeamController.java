package org.example.controller;

import org.example.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamview.dto.NewTeamDTO;
import org.teamview.model.Team;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping(
            value = "/teams",
            produces = "application/json",
            consumes = "application/json"
    )
    public ResponseEntity<?> createNewTeam(@RequestBody NewTeamDTO newTeam) {

        teamService.newTeam(newTeam);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
