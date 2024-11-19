import { Component, ViewChild, HostListener  } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormsModule } from '@angular/forms';
import { TalentSharePortalCategoryService } from 'src/app/services/talent-share-portal-category.service';
import { MatMenuTrigger } from '@angular/material/menu';
import { TalentSharePortalSkillService } from 'src/app/services/talent-share-portal-skill.service';
import { MentorService } from '../search-mentor/search-mentor.service';
import { TalentSharePortalMentorService } from 'src/app/services/talent-share-portal-mentor.service';
import Swal from 'sweetalert2';
import { MentorProfileService } from './mentor-profile.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-list-of-mentors',
  templateUrl: './list-of-mentors.component.html',
  styleUrls: ['./list-of-mentors.component.css'],
})

export class ListOfMentorsComponent {

  showFooter = false;

  @ViewChild(MatMenuTrigger) trigger!: MatMenuTrigger;
  categoryForm!: FormGroup;
  categories: string[] = [];
  skills: string[] = [];
  skillsMenu: any;
  mentors: any[] = [];
  numberOfMentors!: number;
  selectedSkills: string[] = [];
  selectedSkillsNotEmpty: boolean | undefined;
  

  constructor(
    private fb: FormBuilder, 
    private categoryService: TalentSharePortalCategoryService, 
    private skillsService: TalentSharePortalSkillService,
    private sharedMentorService: MentorService,
    private mentorService: TalentSharePortalMentorService,
    private sharedMentorProfileService: MentorProfileService,
    private router: Router 
    ) 
    {}


    @HostListener('window:scroll', ['$event'])
    onScroll() {
      const contentHeight = document.body.scrollHeight;
      const scrollPosition = window.scrollY + window.innerHeight;
      const threshold = 140; // Adjust this threshold as needed
    
      this.showFooter = scrollPosition >= contentHeight - threshold;
    }

ngOnInit() {
    const mentorsData = localStorage.getItem('mentorsData');
    const mentorCount = localStorage.getItem('mentorCount');
    if (mentorsData) {
      this.mentors = JSON.parse(mentorsData);
    }
    if(mentorCount){
      this.numberOfMentors = JSON.parse(mentorCount);
    }
    console.log(this.mentors);
    this.sharedMentorService.mentors$.subscribe((response: any) => {
      if(response && response.data && response.data.noOfMentors)
      {
        this.numberOfMentors = response.data.noOfMentors;
        localStorage.setItem('mentorCount', JSON.stringify(this.numberOfMentors));
      }
      if (response && response.data && response.data.listOfMentors) {
        this.mentors = response.data.listOfMentors;

          // Save mentors data to localStorage
        localStorage.setItem('mentorsData', JSON.stringify(this.mentors));
        console.log('Mentors:', this.mentors);
      } 
      else if (response.error) {
        console.error('Error:', response.error);
        this.error();
      }
    });

      // Fetch skills when the component is initialized
       this.fetchSkills();

         this.mentorService.getallMentorsList().subscribe(
           (result:any) => {
           console.log('Mentor list:', result);
           this.sharedMentorService.setMentors(result);
           this.router.navigate(['/mentor/browse']);
         },
         (error) => {
           console.error('Error: ', error);
           Swal.fire({
             icon: 'error',
             title: 'Oops...',
             text: 'No Mentor found with this skill!',
           })
           this.router.navigate(['/mentor/browse']);
         }
       );
  }
  
  error(){
    Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: 'No Mentor found with this skill!',
    })
  }
  fetchCategories() {
    this.categoryService.getAllCategories().subscribe(
      (response: any) => {
        if (response.status === 200) {
          // Extract 'category' property from the 'data' array in the response
          const responseData = response.data || [];
          this.categories = responseData.map((category: any) => category.category);
        } else {
          console.error('Error:', response.message);
          // Handle the error here, if necessary
        }
      },
      (error) => {
        console.error('Error:', error);
        // Handle the error here
      }
    );
  }
  
  fetchSkills() {
    this.skillsService.getAllSkills().subscribe(
      (response: any) => {
        if (response.status === 200) {
          const responseData = response.data || [];
          this.skills = responseData.map((skill: any) => skill.skill);
        } else {
          console.error('Error:', response.message);
          // Handle the error here, if necessary
        }
      },
      (error) => {
        console.error('Error:', error);
        // Handle the error here
      }
    );
  }

  onSkillsItemClick() {
    console.log("onsikllsitem called"); 
    
     // Check if there are any selected skills
    this.selectedSkillsNotEmpty = this.selectedSkills.length > 0;

    this.mentorService.getMentorsListBySkill(this.selectedSkills).subscribe(
      (response: any) => {
        if(response && response.data && response.data.noOfMentors)
      {
        this.numberOfMentors = response.data.noOfMentors;
        localStorage.setItem('mentorCount', JSON.stringify(this.numberOfMentors));
      }
        if (response && response.data) {
          console.log(response);
          this.mentors = response.data.listOfMentors; // Assuming the mentors are in the 'data' property
          localStorage.setItem('mentorsData', JSON.stringify(this.mentors));
          console.log('Mentors:', this.mentors);
        } else {
          console.error('Error:', response.message);
          // Handle the error here, if necessary
        }
      },
      (error: any) => {
        console.error('Error:', error);
        // Handle the error here
      }
    );
  }
  
   resetFilters() {
      this.selectedSkills = [];
      //Call the function to update the mentor list based on the cleared filters
      this.onSkillsItemClick();
      // localStorage.clear();
    }

    getFormattedImage(base64Image: string, imageFormat: string): string {
      // Define the default MIME type
      let mimeType = 'image/jpeg';
    
      if (imageFormat === 'jpeg') {
        mimeType = 'image/jpeg';
      } else if (imageFormat === 'png') {
        mimeType = 'image/png';
      } else if (imageFormat === 'gif') {
        mimeType = 'image/gif';
      } else if (imageFormat === 'jpg') {
      mimeType = 'image/gif';
      } else if (imageFormat === 'webp') {
      mimeType = 'image/webp';
    }  
      return `data:${mimeType};base64,${base64Image}`;
    }
    serarchHome() {
      this.router.navigate(['mentor/search']);
  }

  viewMentorProfile(empId: number) {
      this.mentorService.viewMentor(empId).subscribe(
        (response: any) => {
          // Handle the response here, e.g., display mentor's profile data
          console.log('Mentor Profile:', response);
          this.sharedMentorProfileService.setMentorProfile(response);
          this.router.navigate(['/mentor/view']);
          // You can update your component's data or navigate to a profile page
        },
        (error: any) => {
          console.error('Error:', error);
          // Handle errors, e.g., show an error message to the user
        }
      );
    }
  
}
