import { NgModule } from '@angular/core';

import { LandingbotSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [LandingbotSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [LandingbotSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class LandingbotSharedCommonModule {}
