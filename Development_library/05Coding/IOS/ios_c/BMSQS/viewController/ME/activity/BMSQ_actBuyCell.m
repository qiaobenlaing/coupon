//
//  BMSQ_actBuyCell.m
//  BMSQC
//
//  Created by liuqin on 16/1/26.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_actBuyCell.h"

@interface BMSQ_actBuyCell ()

@property (nonatomic, strong)UILabel *typeLabel; //类型
@property (nonatomic, strong)UILabel *priceLabel;//价格
@property (nonatomic, strong)UILabel *countLabel;//数量

@property (nonatomic, strong)UIButton *LessLabel;//减
@property (nonatomic, strong)UIButton *addLabel;//加

@end



@implementation BMSQ_actBuyCell

-(id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 45)];
        bgView.backgroundColor = [UIColor whiteColor];
        [self addSubview:bgView];
        
        self.typeLabel= [[UILabel alloc]initWithFrame:CGRectMake(10, 0, 180, 45)];
        self.typeLabel.textColor = APP_TEXTCOLOR;
        self.typeLabel.font = [UIFont systemFontOfSize:13];
        self.typeLabel.text = @"成人票";
        [bgView addSubview:self.typeLabel];
        
        self.priceLabel = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-180, 0, 80, 45)];
        self.priceLabel.textColor = APP_TEXTCOLOR;
        self.priceLabel.font = [UIFont systemFontOfSize:13];
        self.priceLabel.text = @"￥12.00";
        [bgView addSubview:self.priceLabel];
        
        
        UIView *countView = [[UIView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-120, 0,120, 45)];
        countView.backgroundColor = [UIColor clearColor];
        [bgView addSubview:countView];
        
        self.LessLabel =[[UIButton alloc]initWithFrame:CGRectMake(7,10, 25, 25)];
        self.LessLabel.layer.borderWidth=1;
        self.LessLabel.layer.borderColor = [APP_NAVCOLOR CGColor];
        [self.LessLabel setTitle:@"-" forState:0];
        [self.LessLabel setTitleColor:APP_NAVCOLOR forState:0];
        [countView addSubview:self.LessLabel];
        
        
        
        self.addLabel =[[UIButton alloc]initWithFrame:CGRectMake(85,10, 25, 25)];
        self.addLabel.layer.borderWidth=1;
        self.addLabel.layer.borderColor = [APP_NAVCOLOR CGColor];
        [self.addLabel setTitle:@"+" forState:0];
        [self.addLabel setTitleColor:APP_NAVCOLOR forState:0];
        [countView addSubview:self.addLabel];
        
        [self.LessLabel addTarget:self action:@selector(changeCountMothed:) forControlEvents:UIControlEventTouchUpInside];
        [self.addLabel addTarget:self action:@selector(changeCountMothed:) forControlEvents:UIControlEventTouchUpInside];
        self.countLabel = [[UILabel alloc]initWithFrame:CGRectMake(25+7, 0, 85-25, 45)];
        self.countLabel.textColor = APP_TEXTCOLOR;
        self.countLabel.font = [UIFont systemFontOfSize:13];
        self.countLabel.textAlignment = NSTextAlignmentCenter;
        self.countLabel.text = @"0";
        [countView addSubview:self.countLabel];

    }
    return self;
    
}
-(void)setCell:(NSDictionary *)dic count:(NSString *)count indexRow:(int)row{
    self.myRow = row;
    self.typeLabel.text = [dic objectForKey:@"des"];
    self.priceLabel.text = [NSString stringWithFormat:@"￥%@",[dic objectForKey:@"price"]];
    
    self.countLabel.text = count;
    self.LessLabel.tag = row*100+1;
    self.addLabel.tag = row*100+2;
    
}
-(void)changeCountMothed:(UIButton *)button{
    if ([self.buyDelegate respondsToSelector:@selector(clickCountButton:row:)]) {
        [self.buyDelegate clickCountButton:(int)button.tag%100 row:((int)button.tag/100)-1];
    }

}

@end
