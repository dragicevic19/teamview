import { Employee } from "./Employee";
import { Project } from "./Project";

export interface Team {
    id: number;
    name: string;
    lead: Employee;
    members: Employee[];
    project?: Project;
}