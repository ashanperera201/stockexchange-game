import { NgModule } from '@angular/core';
import {
    MatInputModule,
    MatButtonModule,
    MatCheckboxModule,
    MatMenuModule,
    MatIconModule,
    MatDialogModule,
    MatToolbarModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatGridListModule,
    MatButtonToggleModule,
    MatAutocompleteModule,
    MatExpansionModule,
    MatCardModule,
    MatRadioModule,
    MatTableModule,
    MatTooltipModule,
    MatSidenavModule,
    MatDialog,
    MatTabsModule,
    MatChipsModule,
    MatListModule,
    MatStepperModule,
    MatSlideToggleModule
} from '@angular/material'

const materialModules: any[] = [

    MatInputModule,
    MatButtonModule,
    MatCheckboxModule,
    MatMenuModule,
    MatIconModule,
    MatDialogModule,
    MatToolbarModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatGridListModule,
    MatButtonToggleModule,
    MatAutocompleteModule,
    MatExpansionModule,
    MatGridListModule,
    MatNativeDateModule,
    MatCardModule,
    MatRadioModule,
    MatTableModule,
    MatTooltipModule,
    MatSidenavModule,
    MatDialogModule,
    MatTabsModule,
    MatChipsModule,
    MatListModule,
    MatStepperModule,
    MatSlideToggleModule
];

@NgModule({
    imports: materialModules,
    exports: materialModules
})

export class MaterialModule { }
