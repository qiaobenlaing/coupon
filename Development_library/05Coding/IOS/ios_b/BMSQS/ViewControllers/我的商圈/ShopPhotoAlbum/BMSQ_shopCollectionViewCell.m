//
//  BMSQ_shopCollectionViewCell.m
//  BMSQC
//
//  Created by liuqin on 15/9/10.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_shopCollectionViewCell.h"
#import "UIImageView+AFNetworking.h"

@interface BMSQ_shopCollectionViewCell ()

@property (nonatomic, strong)UIImageView *ImageView;
@property (nonatomic, strong)UILabel *contentLabel;

@property (nonatomic, strong)UILabel *messageLabel;
@property (nonatomic, strong)UILabel *priceLabel;

@property (nonatomic, strong)UIView *backView;

@end


@implementation BMSQ_shopCollectionViewCell


-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        
        self.backView = [[UIView alloc]initWithFrame:CGRectMake(15, 10, APP_VIEW_WIDTH/2-30, APP_VIEW_WIDTH/2-3)];
        self.backView.backgroundColor = [UIColor clearColor];
        self.backView.layer.borderWidth = 1;
        self.backView.layer.borderColor =[[UIColor colorWithRed:200/255.0 green:200/255.0 blue:202/255.0 alpha:1]CGColor];
        [self.contentView addSubview:self.backView];
        
        
        self.ImageView =[[UIImageView alloc]initWithFrame:CGRectMake(5, 5, self.backView.frame.size.width-10,self.backView.frame.size.width-10)];
        self.ImageView.backgroundColor = [UIColor clearColor];
        [self.backView addSubview:self.ImageView];
        
        self.contentLabel  = [[UILabel alloc]initWithFrame:CGRectMake(5, self.ImageView.frame.origin.y+self.ImageView.frame.size.height, self.ImageView.frame.size.width, self.backView.frame.size.height-self.ImageView.frame.origin.y-self.ImageView.frame.size.height)];
        self.contentLabel.backgroundColor = [UIColor clearColor];
        self.contentLabel.textAlignment = NSTextAlignmentCenter;
        self.contentLabel.numberOfLines = 0;
        [self.backView addSubview:self.contentLabel];
        
        
        self.messageLabel = [[UILabel alloc]initWithFrame:CGRectMake(5, self.contentLabel.frame.origin.y,self.contentLabel.frame.size.width, self.contentLabel.frame.size.height/2)];
        self.messageLabel.backgroundColor = [UIColor clearColor];
        self.messageLabel.textAlignment = NSTextAlignmentCenter;
        self.messageLabel.font = [UIFont systemFontOfSize:11.f];
        [self.backView addSubview:self.messageLabel];
        
        self.priceLabel = [[UILabel alloc]initWithFrame:CGRectMake(5, self.messageLabel.frame.origin.y+self.messageLabel.frame.size.height,self.contentLabel.frame.size.width, self.contentLabel.frame.size.height/2)];
        self.priceLabel.backgroundColor = [UIColor clearColor];
        self.priceLabel.textAlignment = NSTextAlignmentCenter;
        self.priceLabel.font = [UIFont systemFontOfSize:11.f];

        [self.backView addSubview:self.priceLabel];
        
        
        
    
        
        
        
    }
    return self;
}
-(void)setSubAlbm:(NSDictionary *)dic row:(int)row{
    
    
    NSString *str = [NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[dic objectForKey:@"url"]];
    [self.ImageView setImageWithURL:[NSURL URLWithString:str] placeholderImage:[UIImage imageNamed:@"iv_loadingLogo"]];
    self.messageLabel.text = [dic objectForKey:@"title"];
    self.priceLabel.text = [NSString stringWithFormat:@"￥%@",[dic objectForKey:@"price"]];

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
