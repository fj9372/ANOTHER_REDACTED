import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdoptionBasketComponent } from './adoption-basket.component';

describe('AdoptionBasketComponent', () => {
  let component: AdoptionBasketComponent;
  let fixture: ComponentFixture<AdoptionBasketComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdoptionBasketComponent]
    });
    fixture = TestBed.createComponent(AdoptionBasketComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
