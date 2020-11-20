//
//  BMSQ_homeCircleSquareTableViewCell.m
//  BMSQC
//
//  Created by liuqin on 15/12/4.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_homeCircleSquareTableViewCell.h"

@implementation BMSQ_homeCircleSquareTableViewCell

-(id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.backgroundColor = [UIColor whiteColor];
        self.sc = [[UIScrollView alloc]init];
        self.sc.backgroundColor = [UIColor whiteColor];
        [self addSubview:self.sc];
    }
    return self;
}


-(void)homeCircleSquare:(id)reponse height:(float)height{
    
    for (id aboveView in self.sc.subviews) {
        [aboveView removeFromSuperview];
    }
    
    self.sc.frame = CGRectMake(0, 10, APP_VIEW_WIDTH, height-10);
    NSArray *array = reponse;
    
     float w = array.count>1? APP_VIEW_WIDTH/2:APP_VIEW_WIDTH;
        for (int i= 0; i<array.count; i++) {
            UIImageView *imageV = [[UIImageView alloc]initWithFrame:CGRectMake(i*w,0, w, self.sc.frame.size.height)];
            NSDictionary *dic = [array objectAtIndex:i];
            [imageV sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[dic objectForKey:@"imgUrl"]]] placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];
            [self.sc addSubview:imageV];
            UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(clickImage:)];
            imageV.userInteractionEnabled = YES;
            [imageV addGestureRecognizer:tap];
            imageV.tag = i;
            
        }
        self.sc.contentSize = CGSizeMake(w*array.count, self.sc.frame.size.height);
    
    
}
-(void)clickImage:(UITapGestureRecognizer *)tap{
       [self.circleDelegate clickCircle:(int)tap.view.tag];
}
@end
