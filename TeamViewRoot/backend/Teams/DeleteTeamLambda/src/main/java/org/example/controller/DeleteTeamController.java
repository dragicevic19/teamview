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
public class DeleteTeamController {
    private final TeamService teamService;

    public DeleteTeamController(TeamService teamService) {
        this.teamService = teamService;
    }
    @DeleteMapping(
            value = "/teams",
            produces = "application/json"
    )
    public ResponseEntity<?> deleteTeam(@RequestParam Map<String, String> params) {
        teamService.deleteTeam(params.get("id"));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
