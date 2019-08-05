import { NgModule } from '@angular/core';
import { GridModule, ExcelModule } from '@progress/kendo-angular-grid';
import { DropDownsModule, DropDownListModule } from '@progress/kendo-angular-dropdowns';
import { DateInputsModule } from '@progress/kendo-angular-dateinputs';
import { IntlModule } from '@progress/kendo-angular-intl';
import { UploadModule } from '@progress/kendo-angular-upload';
import { PDFExportModule } from "@progress/kendo-angular-pdf-export";
import { PopupModule } from '@progress/kendo-angular-popup';
import { TreeViewModule } from '@progress/kendo-angular-treeview'
import { ChartsModule } from '@progress/kendo-angular-charts';

const kendoModules: any[] = [
    GridModule,
    DropDownsModule,
    DateInputsModule,
    ExcelModule,
    IntlModule,
    DropDownListModule,
    UploadModule,
    PDFExportModule,
    PopupModule,
    TreeViewModule,
    ChartsModule
];

@NgModule({
    imports: kendoModules,
    exports: kendoModules
})

export class KendoModule { }