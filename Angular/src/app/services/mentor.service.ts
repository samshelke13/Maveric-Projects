import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import {Observable} from 'rxjs';
import { Mentor } from '../models/mentor';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MentorService {

  private baseUrl = `${environment.mentorUrl}:8070/api/v1/mentor`;
  private skillUrl = `${environment.mentorUrl}:8070/api/v1/skill/get-all-skills`;
  private categoriesUrl = `${environment.mentorUrl}:8070/api/v1/category/get-all-categories`;
  
   constructor(private httpClient: HttpClient,
    ) { 
    
  }

  login(employeeId: number, password: string): Observable<any> {
    // Assuming your backend has a login API endpoint
    const params = new HttpParams()
    .set('username', employeeId)
    .set('password', password);
    console.log(params);
    return this.httpClient.get(`${this.baseUrl}/login`, {params});
  }

  addMentor(mentor:Mentor): Observable<Object>{
    return this.httpClient.post(`${this.baseUrl}/register`, mentor);
  }

  getAllSkills(): Observable<any> {
    return this.httpClient.get(`${this.skillUrl}`);
  }

  getAllCategories(): Observable<any> {
    return this.httpClient.get(`${this.categoriesUrl}`);
  }

  getMentorByEmployeeId(id:number): Observable<any> {
    return this.httpClient.get(`${this.baseUrl}/get/${id}`)
  }

  updateMentor(id:Number, mentor:Mentor): Observable<any> {
    return this.httpClient.put(`${this.baseUrl}/update/${id}`, mentor);
  }

  deleteMentorByEmployeeId(id:number): Observable<any> {
    return this.httpClient.delete(`${this.baseUrl}/delete/${id}`)
  }


}