import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Category } from '../models/category';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TalentSharePortalCategoryService {

  private baseURL = `${environment.mentorUrl}:8070/api/v1/category`;

  constructor(private httpClient: HttpClient) {}

  getAllCategories(): Observable<Category> {
    const url = `${this.baseURL}/get-all-categories`;
    return this.httpClient.get<Category>(url);
  }
}

