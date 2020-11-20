//
//  ProctionCollectionViewCell.m
//  BMSQC
//
//  Created by liuqin on 15/9/11.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "ProctionCollectionViewCell.h"


@interface ProctionCollectionViewCell ()

@property (nonatomic, strong)UIView *backView;
@property (nonatomic, strong)UIImageView *ImageView;
@property (nonatomic, strong)UILabel *contentLabel;
@property (nonatomic, strong)UILabel *countLabel;

@end


@implementation ProctionCollectionViewCell



-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        
        self.backView = [[UIView alloc]initWithFrame:CGRectMake(20, 10, APP_VIEW_WIDTH/2-40, APP_VIEW_WIDTH/2-40)];
        self.backView.backgroundColor = [UIColor whiteColor];
        self.backView.layer.borderWidth = 1;
        self.backView.layer.borderColor =[[UIColor colorWithRed:200/255.0 green:200/255.0 blue:202/255.0 alpha:1]CGColor];
        [self.contentView addSubview:self.backView];
        
        
        self.ImageView =[[UIImageView alloc]initWithFrame:CGRectMake(5, 5, self.backView.frame.size.width-10, self.backView.frame.size.width-10)];
        self.ImageView.backgroundColor = [UIColor clearColor];
        [self.backView addSubview:self.ImageView];
        
        self.contentLabel  = [[UILabel alloc]initWithFrame:CGRectMake(5, self.ImageView.frame.size.height-20, self.ImageView.frame.size.width-10, 20)];
        self.contentLabel.backgroundColor = [UIColor whiteColor];
        self.contentLabel.alpha = 0.613;
        self.contentLabel.textAlignment = NSTextAlignmentCenter;
        self.contentLabel.font= [UIFont systemFontOfSize:13.f];
        self.contentLabel.numberOfLines = 0;
        [self.ImageView addSubview:self.contentLabel];
        
        
        
        self.countLabel = [[UILabel alloc]initWithFrame:CGRectMake(self.ImageView.frame.size.width-30 ,10, 30, 20)];
        self.countLabel.backgroundColor = [UIColor whiteColor];
        self.countLabel.alpha = 0.613;
        self.countLabel.textAlignment = NSTextAlignmentCenter;
        self.countLabel.font= [UIFont systemFontOfSize:13.f];
        self.countLabel.layer.masksToBounds = YES;
        self.countLabel.layer.cornerRadius = 10;
        self.countLabel.numberOfLines = 0;
        [self.ImageView addSubview:self.countLabel];

        
        
    }
    return self;
}


-(void)setCollectionCell:(NSDictionary *)dic row:(int)row{
    
    
    NSString *str = [NSString stringWithFormat:@"%@%@",IMAGE_URL,[dic objectForKey:@"url"]];
//    [self.ImageView setImageWithURL:[NSURL URLWithString:str] placeholderImage:[UIImage imageNamed:@"iv_loadingLogo"]];
    
    [self.ImageView sd_setImageWithURL:[NSURL URLWithString:str] placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];
    
    UIImage *image = self.ImageView.image;
    NSLog(@"str,size =%@ , %@",str,NSStringFromCGSize(image.size) );
    
    self.contentLabel.text = [dic objectForKey:@"name"];
    self.countLabel.text = [NSString stringWithFormat:@"%@",[dic objectForKey:@"photoCount"]];
    
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
