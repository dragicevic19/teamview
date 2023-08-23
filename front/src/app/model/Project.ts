import { Team } from "./Team";

export interface Project {
    id: string;
    title: string;
    description: string;
    team?: Team;
    client: string;
    status: string;
    startDate: Date;
    endDate: Date;
}