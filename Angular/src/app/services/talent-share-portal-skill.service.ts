import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Skill } from '../models/Skill';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TalentSharePortalSkillService {

  private baseURL = `${environment.mentorUrl}:8070/api/v1/skill`;

  constructor(private httpClient: HttpClient) {}

  getAllSkills(): Observable<Skill> {
    const url = `${this.baseURL}/get-all-skills`;
    return this.httpClient.get<Skill>(url);
  }

}
