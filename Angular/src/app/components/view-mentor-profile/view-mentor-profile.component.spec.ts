import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewMentorProfileComponent } from './view-mentor-profile.component';

describe('ViewMentorProfileComponent', () => {
  let component: ViewMentorProfileComponent;
  let fixture: ComponentFixture<ViewMentorProfileComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ViewMentorProfileComponent]
    });
    fixture = TestBed.createComponent(ViewMentorProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
