import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'seniorityToBadge'
})
export class SeniorityToBadgePipe implements PipeTransform {

  transform(value: string): string {
    switch (value) {
      case 'Junior': return 'badge-success'
      case 'Medior': return 'badge-info'
      case 'Senior': return 'badge-danger'
    }
    return value;
  }

}
