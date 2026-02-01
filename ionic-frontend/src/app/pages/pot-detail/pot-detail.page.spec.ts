import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PotDetailPage } from './pot-detail.page';

describe('PotDetailPage', () => {
  let component: PotDetailPage;
  let fixture: ComponentFixture<PotDetailPage>;

  beforeEach(() => {
    fixture = TestBed.createComponent(PotDetailPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
