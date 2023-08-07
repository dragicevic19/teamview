import { Team } from "./Team";

export interface Project {
    id: number;
    title: string;
    lead?: string;
    leadsMail?: string;
    team?: string;
    client: string;
    status: string;
    startDate: Date;
    endDate: Date;
}