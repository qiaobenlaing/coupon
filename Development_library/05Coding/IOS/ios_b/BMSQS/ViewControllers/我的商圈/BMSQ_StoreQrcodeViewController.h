//
//  BMSQ_StoreQrcodeViewController.h
//  BMSQS
//
//  Created by lxm on 15/8/9.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BaseViewController.h"

@interface BMSQ_StoreQrcodeViewController : BaseViewController
{
    __weak IBOutlet UIImageView *_logoImageView;
    __weak IBOutlet UILabel *_nameLabel;
    __weak IBOutlet UILabel *_addressLabel;
    
    __weak IBOutlet UIImageView *_qrcodeImageView;
}
@property (nonatomic,strong)NSDictionary *shopAllMsg;

@end
