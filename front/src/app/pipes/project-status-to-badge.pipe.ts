import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'projectStatusToBadge'
})
export class ProjectStatusToBadgePipe implements PipeTransform {

  transform(value: string): string {
    switch(value) {
      case 'IN_PROGRESS': return 'badge-primary'
      case 'ON_HOLD': return 'badge-info'
      case 'COMPLETED': return 'badge-success'
    }
    return value;
  }

}
