package org.example.controller;

import org.example.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
//    public ResponseEntity<?> getAllTeams(@RequestParam Map<String, String> params,
//                                         @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {

    public ResponseEntity<?> getAllTeams(@RequestParam Map<String, String> params) {

//        String sub = JWTUtil.getSub(accessToken, "sub");
//        if (sub == null)
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Team> teams = teamService.getTeams();
        return new ResponseEntity<>(teams, HttpStatus.OK);

//        if (params.containsKey("bought")) {
//            Collection<BoughtProduct> boughtProducts = productService.getBoughtProductsByUserId(sub);
//            return new ResponseEntity<>(boughtProducts, HttpStatus.OK);
//
//        } else if (params.isEmpty()) {
//            List<Product> productsList = productService.getProducts();
//            return new ResponseEntity<>(productsList, HttpStatus.OK);
//        } else
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
