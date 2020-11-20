//
//  CreatQRView.h
//  BMSQC
//
//  Created by liuqin on 16/1/13.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol CreatQRViewDelegate <NSObject>

-(void)refreshQR;

@end



@interface CreatQRView : UIView


@property (nonatomic, strong)UIImage *imageHead;
@property (nonatomic, weak)id<CreatQRViewDelegate>delegate;
-(void)creatCode:(NSString *)qrStr;

@end
