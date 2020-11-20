//
//  BMSQ_StoreQrcodeViewController.h
//  BMSQS
//
//  Created by lxm on 15/8/9.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BaseViewController.h"

@interface BMSQ_StoreQrcodeViewController : UIViewControllerEx
{
    UIImageView *_logoImageView;
    UILabel *_nameLabel;
    UILabel *_addressLabel;
    
    UIImageView *_qrcodeImageView;
}
@property (nonatomic,strong)NSDictionary *shopAllMsg;

@end
