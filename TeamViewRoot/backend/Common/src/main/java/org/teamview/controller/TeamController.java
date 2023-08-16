package org.teamview.controller;


import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamview.dto.EmployeeDTO;
import org.teamview.dto.NewTeamDTO;
import org.teamview.dto.PaginationDTO;
import org.teamview.dto.TeamDTO;
import org.teamview.service.TeamService;

@RestController
@RequestMapping(value = "/teams")
@CrossOrigin
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping(value = "/{page}/{rows}")
    public ResponseEntity<?> getTeams(@PathVariable int page, @PathVariable int rows) {
        PaginationDTO<TeamDTO> retVal = this.teamService.findAll(PageRequest.of(page, rows));
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> newTeam(@RequestBody NewTeamDTO newTeam) {

        TeamDTO retVal = this.teamService.newTeam(newTeam);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> editTeam(@PathVariable Long id, @RequestBody NewTeamDTO editTeam) {

        TeamDTO retVal = this.teamService.editTeam(id, editTeam);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long id) {
        this.teamService.deleteTeam(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}