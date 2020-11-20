//
//  BMSQ_homeTypeCell.m
//  BMSQC
//
//  Created by liuqin on 15/12/4.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_homeTypeCell.h"


@implementation BMSQ_homeTypeView

-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        self.typeImage = [[UIImageView alloc]initWithFrame:CGRectMake(2, 0, frame.size.width-4, frame.size.width-4)];
        self.typeImage.layer.cornerRadius =self.typeImage.frame.size.width/2;
        self.typeImage.layer.masksToBounds = YES;
        self.typeImage.backgroundColor = [UIColor clearColor];
        [self addSubview:self.typeImage];
        
        self.typeLabel = [[UILabel alloc]initWithFrame:CGRectMake(0,self.typeImage.frame.origin.y+self.typeImage.frame.size.height, frame.size.width, frame.size.height-self.typeImage.frame.size.height)];
        self.typeLabel.textColor = APP_TEXTCOLOR;
        self.typeLabel.textAlignment = NSTextAlignmentCenter;
        self.typeLabel.font = [UIFont systemFontOfSize:12];
        self.typeLabel.backgroundColor = [UIColor clearColor];
        [self addSubview:self.typeLabel];
        
    }
    return self;
}

-(void)setTypeV:(NSURL *)ImageUrl label:(NSString *)title{
    [self.typeImage sd_setImageWithURL:ImageUrl placeholderImage:[UIImage imageNamed:@"iv_logNodata"]];
    
    self.typeLabel.text = title;
    
}
@end





@implementation BMSQ_homeTypeCell

-(id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
     
    }
    return self;
}
-(void)setHomeTypeCell:(id)reponse height:(float)height{
    for (UIView *aboveView in self.subviews) {
        [aboveView removeFromSuperview];
    }
    NSArray *subList = reponse;
    float spx = 15;
      float imageW = ((APP_VIEW_WIDTH-30)-4*spx)/(subList.count>4?5:subList.count);
        UIScrollView *bgView = [[UIScrollView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, height)];
        bgView.backgroundColor  = [UIColor whiteColor];
        [self addSubview:bgView];
        for (int i=0; i<subList.count; i++) {
            NSDictionary *dic = [subList objectAtIndex:i];
            BMSQ_homeTypeView *typeView = [[BMSQ_homeTypeView alloc]initWithFrame:CGRectMake(15+i*spx+i*imageW, 8, imageW, bgView.frame.size.height-10)];
            [typeView setTypeV:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[dic objectForKey:@"imgUrl"]]] label:[dic objectForKey:@"title"]];
            typeView.tag = i;
            typeView.typeDic = dic;
            typeView.userInteractionEnabled = YES;
            UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(clickTap:)];
            [typeView addGestureRecognizer:tap];
            [bgView addSubview:typeView];
        }
        
        bgView.contentSize = CGSizeMake(subList.count*imageW+subList.count*spx+subList.count+15, bgView.frame.size.height);
}
-(void)clickTap:(UITapGestureRecognizer *)tap{
    int i = (int)tap.view.tag;
    BMSQ_homeTypeView *typeView = (BMSQ_homeTypeView *)tap.view;
    [self.typeDelegate clickType:i typeDic:typeView.typeDic];
}

@end
