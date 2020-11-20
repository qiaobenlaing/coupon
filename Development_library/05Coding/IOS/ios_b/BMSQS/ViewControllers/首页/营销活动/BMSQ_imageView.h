//
//  BMSQ_imageView.h
//  BMSQS
//
//  Created by liuqin on 15/10/28.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol BMSQ_imageViewDelegate <NSObject>

-(void)deleActImage:(int)tag;

@end





@interface BMSQ_imageView : UIView

@property (nonatomic, strong)id<BMSQ_imageViewDelegate>delegate;
@property (nonatomic, strong)UIImageView *BgimageView;
@property (nonatomic, strong)UIButton *delebutton;
@end
