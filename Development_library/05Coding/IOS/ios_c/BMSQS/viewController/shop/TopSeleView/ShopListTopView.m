//
//  ShopListTopView.m
//  BMSQC
//
//  Created by liuqin on 15/12/2.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "ShopListTopView.h"


@interface ShopListTopView ()



@end

@implementation ShopListTopView

-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = UICOLOR(245, 245, 245, 1) ;
        self.userInteractionEnabled = YES;
        
        float w = APP_VIEW_WIDTH/4;
        NSArray *array = @[@"区域",@"行业",@"智能排序",@"筛选"];
        self.titleArray = [[NSMutableArray alloc]initWithArray:array];
        for (int i=0; i<4; i++) {
            UIView *view = [[UIView alloc]initWithFrame:CGRectMake(i*w, 0, w, frame.size.height)];
            view.backgroundColor = [UIColor clearColor];
            [self addSubview:view];
            
            
            UILabel *label = [[UILabel alloc]init];
            label.backgroundColor = [UIColor clearColor];
            label.textColor = UICOLOR(11, 11, 11, 1);
            label.font = [UIFont systemFontOfSize:12];
            label.text = [array objectAtIndex:i];
            label.tag = 100;
            [view addSubview:label];
            
            NSDictionary *attribute = @{NSFontAttributeName: [UIFont systemFontOfSize:12]};
            CGSize size = [label.text boundingRectWithSize:CGSizeMake(MAXFLOAT,MAXFLOAT) options: NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading attributes:attribute context:nil].size;
            label.frame = CGRectMake((w-size.width)/2, (frame.size.height-size.height)/2, size.width, size.height);
            
            UIImageView *imageView = [[UIImageView alloc]initWithFrame:CGRectMake(label.frame.origin.x+label.frame.size.width+1, (frame.size.height-5)/2, 8, 5)];
            [imageView setImage:[UIImage imageNamed:@"down_gray"]];
            imageView.tag = 200;
            [view addSubview:imageView];
            view.tag = 1000+i;
            
            UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(clickTopView:)];
            [view addGestureRecognizer:tapGesture];
            
        }
        
    }
    return self;
}
-(void)clickTopView:(UITapGestureRecognizer *)tapGesture{
    
    UIView *seleview = tapGesture.view;

    for (int i = 1000; i<1004; i++) {
        UIView *view = [self viewWithTag:i];
        if (seleview.tag == view.tag) {
            UIImageView *imageView = (UIImageView *)[view viewWithTag:200];
            [imageView setImage:[UIImage imageNamed:@"up_red"]];
        }else{
            UIImageView *imageView = (UIImageView *)[view viewWithTag:200];
            [imageView setImage:[UIImage imageNamed:@"down_gray"]];
        }
    }

    [self.listDelegate showListDataDelegate:(int)seleview.tag];

}
-(void)changeTitle:(NSString *)str tag:(int)tag{
    
    float w = APP_VIEW_WIDTH/4;

    for (int i = 1000; i<1004; i++) {
        UIView *view = [self viewWithTag:i];
        if (tag == view.tag) {
            UILabel *label = (UILabel *)[view viewWithTag:100];
            label.text = str;
            label.textColor = APP_NAVCOLOR;
            NSDictionary *attribute = @{NSFontAttributeName: [UIFont systemFontOfSize:12]};
            CGSize size = [label.text boundingRectWithSize:CGSizeMake(MAXFLOAT,MAXFLOAT) options: NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading attributes:attribute context:nil].size;
            label.frame = CGRectMake((w-size.width)/2, (self.frame.size.height-size.height)/2, size.width, size.height);
            
            UIImageView *imageView = (UIImageView *)[view viewWithTag:200];
            imageView.frame = CGRectMake(label.frame.origin.x+label.frame.size.width+1, (self.frame.size.height-5)/2, 8, 5);
            [imageView setImage:[UIImage imageNamed:@"up_red"]];
        }
        else{

            UILabel *label =(UILabel *) [view viewWithTag:100];
            label.textColor =  UICOLOR(11, 11, 11, 1);
            NSDictionary *attribute = @{NSFontAttributeName: [UIFont systemFontOfSize:12]};
            CGSize size = [label.text boundingRectWithSize:CGSizeMake(MAXFLOAT,MAXFLOAT) options: NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading attributes:attribute context:nil].size;
            label.frame = CGRectMake((w-size.width)/2, (self.frame.size.height-size.height)/2, size.width, size.height);

            
            
            UIImageView *imageView = (UIImageView *)[view viewWithTag:200];
            [imageView setImage:[UIImage imageNamed:@"down_gray"]];
        }
    }
}

@end
