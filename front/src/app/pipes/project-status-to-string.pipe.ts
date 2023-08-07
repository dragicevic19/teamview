import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'projectStatusToString'
})
export class ProjectStatusToStringPipe implements PipeTransform {

  transform(value: string): string {
    switch(value) {
      case 'IN_PROGRESS': return 'In Progress'
      case 'ON_HOLD': return 'On Hold'
      case 'COMPLETED': return 'Completed'
    }
    return value;
  }

}
