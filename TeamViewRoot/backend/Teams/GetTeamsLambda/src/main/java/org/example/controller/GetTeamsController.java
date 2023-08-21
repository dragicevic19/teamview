package org.example.controller;

import org.example.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamview.dto.TeamDTO;
import org.teamview.model.Team;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class GetTeamsController {
    private final TeamService teamService;

    public GetTeamsController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping(
            value = "/teams",
            produces = "application/json"
    )
    public ResponseEntity<?> getAllTeams(@RequestParam Map<String, String> params) {
        List<TeamDTO> teams = teamService.getTeams();
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }
}
