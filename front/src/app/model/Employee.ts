import { Project } from "./Project";
import { Team } from "./Team";

export interface Employee {
    id: string;
    name: string;
    email: string;
    project?: Project;
    team?: Team;
    position: string;
    seniority: string; 
    lead: boolean;
    allProjects?: Project[];
    address: string;
    teamId?: string;
}