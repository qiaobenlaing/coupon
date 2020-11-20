//
//  BMSQ_AgreementViewController.h
//  BMSQS
//
//  Created by lxm on 15/7/25.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BaseViewController.h"

@interface BMSQ_AgreementViewController : BaseViewController
{
    __weak IBOutlet UIWebView *_webView;
}
@property(nonatomic,strong)NSString *url;
@property(nonatomic,strong)NSString *navTitle;
@end
