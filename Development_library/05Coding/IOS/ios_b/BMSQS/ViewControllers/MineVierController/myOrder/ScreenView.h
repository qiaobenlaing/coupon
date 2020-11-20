//
//  ScreenView.h
//  BMSQS
//
//  Created by  on 15/12/2.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol ScreenViewDelegate <NSObject>

-(void)clickBg;
-(void)seleData:(NSString *)status;
@end

@interface ScreenView : UIView

@property (nonatomic, weak) id <ScreenViewDelegate> scDelegate;

@end
