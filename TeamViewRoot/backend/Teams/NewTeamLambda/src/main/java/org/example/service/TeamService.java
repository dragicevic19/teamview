package org.example.service;

import com.github.f4b6a3.ulid.UlidCreator;
import org.springframework.stereotype.Service;
import org.teamview.dto.EmployeeDTO;
import org.teamview.dto.NewTeamDTO;
import org.teamview.model.Team;
import org.teamview.model.User;
import org.teamview.repository.DynamoBuilder;

import java.util.List;

@Service
public class TeamService {

    public void newTeam(NewTeamDTO newTeam) {
        DynamoBuilder repo = DynamoBuilder.createBuilder();

        Team team = new Team();
        team.setId(UlidCreator.getUlid().toLowerCase());
        team.setTeamName(newTeam.getName());

        for (EmployeeDTO user : newTeam.getMembers()) {
            User usr = new User();
            usr.setId(user.getId());

            String teamId = (user.getTeam() != null && user.getTeam().getId() != null) ? user.getTeam().getId() : null;
            User dynamoUser = repo.getUser(user.getId(), teamId);
            repo.deleteUser(dynamoUser); // moram da ga obrisem da bih promenio PK (teamId)?

            dynamoUser.setTeamId(team.getId()); // new team
            dynamoUser.setTeamLead(false);
            if (user.getId().equals(newTeam.getLead().getId())) {
                dynamoUser.setTeamLead(true);
                team.setTeamLead(dynamoUser);
                // todo: check if he already was team lead for previous team and clear that
            }
            repo.saveUser(dynamoUser);
        }

        repo.saveTeam(team);
    }
}
