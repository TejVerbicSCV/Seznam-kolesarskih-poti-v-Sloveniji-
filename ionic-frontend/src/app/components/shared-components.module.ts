import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouteEditorComponent } from './route-editor/route-editor.component';
import { PasswordChangeComponent } from './password-change/password-change.component';

@NgModule({
    declarations: [RouteEditorComponent, PasswordChangeComponent],
    imports: [
        CommonModule,
        IonicModule,
        FormsModule,
        ReactiveFormsModule
    ],
    exports: [RouteEditorComponent, PasswordChangeComponent]
})
export class SharedComponentsModule { }
