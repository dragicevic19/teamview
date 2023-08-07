import { Project } from "./Project";

export interface Team {
    id: number;
    name: string;
    lead: string;
    leadsMail: string;
    members: number;
    project: Project;
}