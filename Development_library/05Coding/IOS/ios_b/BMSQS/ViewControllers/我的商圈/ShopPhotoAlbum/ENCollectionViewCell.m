//
//  ENCollectionViewCell.m
//  BMSQC
//
//  Created by liuqin on 15/9/11.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "ENCollectionViewCell.h"

#import "UIImageView+AFNetworking.h"

@interface ENCollectionViewCell ()
@property (nonatomic, strong)UIView *backView;
@property (nonatomic, strong)UIImageView *ImageView;
@property (nonatomic, strong)UILabel *contentLabel;

@end


@implementation ENCollectionViewCell



-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        
        
        self.backView = [[UIView alloc]initWithFrame:CGRectMake(20, 10, APP_VIEW_WIDTH/2-30, APP_VIEW_WIDTH/2)];
        self.backView.backgroundColor = [UIColor whiteColor];
        self.backView.layer.borderWidth = 0.5;
        self.backView.layer.borderColor =[[UIColor colorWithRed:200/255.0 green:200/255.0 blue:202/255.0 alpha:1]CGColor];
        self.backView.backgroundColor = [UIColor clearColor];
        self.backView.layer.cornerRadius = 3;
        self.backView.layer.masksToBounds = YES;
        [self.contentView addSubview:self.backView];
        
        
        self.ImageView =[[UIImageView alloc]initWithFrame:CGRectMake(5, 5, self.backView.frame.size.width-10,self.backView.frame.size.width-10)];
        self.ImageView.backgroundColor = [UIColor clearColor];
        [self.backView addSubview:self.ImageView];
        
        self.contentLabel  = [[UILabel alloc]initWithFrame:CGRectMake(5, self.ImageView.frame.origin.y+self.ImageView.frame.size.height, self.ImageView.frame.size.width, self.backView.frame.size.height-self.ImageView.frame.origin.y-self.ImageView.frame.size.height)];
        self.contentLabel.backgroundColor = [UIColor clearColor];
        self.contentLabel.textAlignment = NSTextAlignmentCenter;
        self.contentLabel.textColor = [UIColor grayColor];
        self.contentLabel.numberOfLines = 0;
        [self.backView addSubview:self.contentLabel];
        
        
        
    }
    return self;
}
-(void)setEnCell:(NSDictionary *)dic row:(int)row{
    NSString *str = [NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[dic objectForKey:@"imgUrl"]];
    [self.ImageView setImageWithURL:[NSURL URLWithString:str] placeholderImage:[UIImage imageNamed:@"iv_loadingLogo"]];
    self.contentLabel.text = [dic objectForKey:@"title"];
    if (row%2== 0) {
        [self setFrameBackView:YES];
    }else{
        [self setFrameBackView:NO];
        
    }
    
}
-(void)setFrameBackView:(BOOL)boola{
    
    CGRect rect = self.backView.frame;
    float w = (APP_VIEW_WIDTH-(rect.size.width * 2))/6;
    if (boola) {
        self.backView.frame = CGRectMake(2*w, 0, rect.size.width, rect.size.height);
    }else{
        self.backView.frame = CGRectMake(w, 0, rect.size.width, rect.size.height);
        
    }
    
    
    
}
@end
