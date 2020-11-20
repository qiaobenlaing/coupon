//
//  BMSQ_seleDateView.h
//  BMSQC
//
//  Created by liuqin on 16/3/31.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>




@protocol BMSQ_seleDateViewDelegate <NSObject>

-(void)seleDate:(NSString *)dateStr isSubmit:(BOOL)isSubmit status:(BOOL)status;  //yes 开始日期  no 结束日期


@end


@interface BMSQ_seleDateView : UIView

@property (nonatomic, strong)UILabel *remakLabel;
@property (nonatomic, assign)BOOL status;

@property (nonatomic, weak)id<BMSQ_seleDateViewDelegate>delegate;


@end
