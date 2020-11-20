//
//  BMSQ_HomeModuleCell.m
//  BMSQC
//
//  Created by gh on 15/11/26.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_HomeModuleCell.h"
#import "XLCycleScrollView.h"
#import "BMSQ_HomeShopTypeView.h"
#import "BMSQ_HomeBusinessCircleView.h"
#import "BMSQ_HomeBrandView.h"
#import "BMSQ_HomeActivityView.h"

@interface BMSQ_HomeModuleCell()<XLCycleScrollViewDatasource, XLCycleScrollViewDelegate, HomeShopTypeViewDelegate, BusinessCircleDelegate, BrandViewDelegate, HomeActivityViewDelegate>
{
    XLCycleScrollView *ADScrollView;
    NSMutableArray*  m_adDataSource; //顶部滚动广告数组
    NSMutableArray*  m_adImageViewCacheArray; //广告图片缓存
    
}


@property(nonatomic,strong)BMSQ_HomeShopTypeView *shopTypeView;
@property(nonatomic,strong)BMSQ_HomeBusinessCircleView *businessView;


@end

@implementation BMSQ_HomeModuleCell 


- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        [self setViewUp];
        
        
    }
    
    return self;
}

- (void)setViewUp {
    
    self.backgroundColor = [UIColor clearColor];
    
    if (iPhone4) {
        ADScrollView= [[XLCycleScrollView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 140)];
        
    }else{
        ADScrollView= [[XLCycleScrollView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 160)];
        
    }
    ADScrollView.delegate=self;
    ADScrollView.datasource=self;
    [self addSubview:ADScrollView];
    
    
    self.shopTypeView = [[BMSQ_HomeShopTypeView alloc] init];
    [self addSubview:self.shopTypeView];
    
    self.businessView = [[BMSQ_HomeBusinessCircleView alloc] init];
    [self addSubview:self.businessView];
    
    

}

- (void)setHomeModuleCellValue:(NSArray *)dataSource {
    
    CGFloat topCellHeight = 0.0;
    for (NSDictionary *dic in dataSource) {
        
        NSLog(@"%@",dic);
        int moduleValue = [[dic objectForKey:@"moduleValue"] intValue];
        
        switch (moduleValue) {
            case 1: {//滚屏
                m_adDataSource = [dic objectForKey:@"subList"];
                for (NSDictionary* adObject in m_adDataSource){
                    UIImageView* imageView = [[UIImageView alloc]initWithFrame:CGRectMake(0, topCellHeight, APP_VIEW_WIDTH, 160)];
                    [imageView setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[adObject objectForKey:@"imgUrl"]]] placeholderImage:[UIImage imageNamed:@"iv_noDataHome"]];
                    [m_adImageViewCacheArray addObject:imageView];
                    
                    
                }
                [ADScrollView reloadData];

                
                CGRect adFrame = ADScrollView.frame;
                adFrame.origin = CGPointMake(0, topCellHeight);
                ADScrollView.frame = adFrame;
                
                topCellHeight = topCellHeight+ADScrollView.frame.size.height;
                topCellHeight = topCellHeight+10;
                
                
            }
                break;
                
            case 2: { //分类
                
                float btnHeight = 31;
                
                BMSQ_HomeShopTypeView *view = [[BMSQ_HomeShopTypeView alloc] initWithFrame:CGRectMake(0, topCellHeight, APP_VIEW_WIDTH, btnHeight+20*2)];
                view.m_dataSource = [dic objectForKey:@"subList"];
                [view setViewValue:dic];
                view.ShopTypeDelegate = self;
                
                [self.contentView addSubview:view];
                
                topCellHeight = topCellHeight+31+20+20;
                
                topCellHeight = topCellHeight+10;
            }
                break;
                
            case 3: { //商圈
                
                NSArray *array = [dic objectForKey:@"subList"];
                
                BMSQ_HomeBusinessCircleView *view = [[BMSQ_HomeBusinessCircleView alloc] initWithFrame:CGRectMake(0, topCellHeight, APP_VIEW_WIDTH, APP_VIEW_WIDTH/2.5)];
                [view setBusinessCircleViewUp:dic];
                view.delegate = self;
                [self.contentView addSubview:view];
                
                if (array.count == 1) {
                    view.frame = CGRectMake(0, topCellHeight, APP_VIEW_WIDTH, HOME_MODULE_HEIGHT*2);
                    topCellHeight = topCellHeight+HOME_MODULE_HEIGHT*2;
                    
                }else if (array.count == 2) {
                    view.frame = CGRectMake(0, topCellHeight, APP_VIEW_WIDTH, HOME_MODULE_HEIGHT);
                    topCellHeight = topCellHeight+HOME_MODULE_HEIGHT;
                    
                }else if (array.count == 4) {
                    view.frame = CGRectMake(0, topCellHeight, APP_VIEW_WIDTH, HOME_MODULE_HEIGHT*2);
                    topCellHeight = topCellHeight+HOME_MODULE_HEIGHT*2;
                    
                    
                }else if (array.count == 6) {
                    view.frame = CGRectMake(0, topCellHeight, APP_VIEW_WIDTH, HOME_MODULE_HEIGHT*3);
                    topCellHeight = topCellHeight+HOME_MODULE_HEIGHT*3;
                    
                    
                }
                
                topCellHeight = topCellHeight+10;
            }
                break;
             
            case 4: { //品牌
                NSArray *array = [dic objectForKey:@"subList"];
                
                BMSQ_HomeBrandView *view = [[BMSQ_HomeBrandView alloc] initWithFrame:CGRectMake(0, topCellHeight, APP_VIEW_WIDTH, HOME_MODULE_HEIGHT)];
                [view setBrandView:dic];
                view.delegate = self;
                
                [self.contentView addSubview:view];
                
                
                if (array.count == 1) {
                    view.frame = CGRectMake(0, topCellHeight, APP_VIEW_WIDTH, HOME_MODULE_HEIGHT);
                    topCellHeight = topCellHeight + HOME_MODULE_HEIGHT;
                    
                }else if (array.count == 2) {
                    view.frame = CGRectMake(0, topCellHeight, APP_VIEW_WIDTH, HOME_MODULE_HEIGHT);
                    topCellHeight = topCellHeight + HOME_MODULE_HEIGHT;
                    
                }else if(array.count == 4) {
                    view.frame = CGRectMake(0, topCellHeight, APP_VIEW_WIDTH, HOME_MODULE_HEIGHT*2);
                    topCellHeight = topCellHeight + HOME_MODULE_HEIGHT*2;
                    
                }
                topCellHeight = topCellHeight+10;
                
            }
                break;
              
                
            case 5:{ //活动
                
                BMSQ_HomeActivityView *view = [[BMSQ_HomeActivityView alloc] init];
                [view setHomeActivityView:dic];
                [self.contentView addSubview:view];
                view.delegate = self;
                CGRect actframe = view.frame;
                actframe.origin = CGPointMake(0, topCellHeight);
                view.frame = actframe;
                topCellHeight = topCellHeight+view.frame.size.height;
                
                topCellHeight = topCellHeight+10;
                
            }
                break;
              
            default:
                break;
        }

    }

}


#pragma mark - AD data source
- (NSInteger)numberOfPagesScrollView:(XLCycleScrollView *)scrollView
{
    return  m_adDataSource.count;
    
}

- (UIView *)scrollView:(XLCycleScrollView*)scrollView PageAtIndex:(NSInteger)index
{
    
    
    if (index >= m_adImageViewCacheArray.count)
    {
        index = m_adImageViewCacheArray.count - 1;
    }
    
    UIImageView* tmpImage = (UIImageView*)m_adImageViewCacheArray[index];
    
    UIImageView* imageView = [[UIImageView alloc]init];
    if (tmpImage.image == nil)
    {
        imageView.image = [UIImage imageNamed:@"iv_noDataHome"];
        imageView.contentMode = UIViewContentModeScaleToFill;
    }
    else
    {
        imageView.image = tmpImage.image;
        imageView.contentMode = UIViewContentModeScaleToFill;
    }
    
    if (iPhone4) {
        [imageView setFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 140)];
    }else{
        [imageView setFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 160)];
    }
    return imageView;
    
    
}

-(void)didScrollToPage:(NSInteger)page{
    
    //    if (m_adDataSource.count<=0) {
    //        return;
    //    }
    
    if (page >= m_adDataSource.count)
    {
        page = m_adDataSource.count-1;
    }
    
}

- (void)didClickPage:(XLCycleScrollView *)csView atIndex:(NSInteger)index
{
    
    if (m_adDataSource.count <=0) {
        return;
    }
    
    NSDictionary* dic = [m_adDataSource objectAtIndex:index];
    if (self.delegate != nil) {
        
        [self.delegate ClickCSView:dic];
    }

    

}

#pragma mark - shopTypeDelegate
- (void)btnShopTypeClick:(id)object {
    if (self.delegate != nil) {
        [self.delegate gotoLinkType:object];
    }
    
}

#pragma mark - HomeActivityView Delegate
//--首页活动
- (void)clickHomeActivity:(id)object {
    if (self.delegate != nil) {
        [self.delegate gotoLinkType:object];
    }
    
}

#pragma mark - BrandViewDelegate
//--品牌
- (void)touchBrandView:(id)object {
    if (self.delegate != nil) {
        [self.delegate gotoLinkType:object];
    }
    
}

//--商圈
- (void)clickBusinessView:(id)object {
    if (self.delegate != nil) {
        [self.delegate gotoLinkType:object];
    }
    
}

+ (CGFloat)homeModuleCellHeight:(id)dataSource{
    CGFloat TOPHeight = 0.0;
    
    for (NSDictionary *dic in dataSource) {
        
        NSLog(@"%@",dic);
        int moduleValue = [[dic objectForKey:@"moduleValue"] intValue];
        
        switch (moduleValue) {
            case 1:
                TOPHeight = TOPHeight+160+10;
                break;
                
            case 2: {
                TOPHeight = TOPHeight+31+20+20+10;
            }
                break;
                
            case 3: {
                
                NSArray *array = [dic objectForKey:@"subList"];
                
                if (array.count == 2) {
                    
                    TOPHeight = TOPHeight+HOME_MODULE_HEIGHT;
                    
                }else if (array.count == 1 || array.count == 4) {
                    TOPHeight = TOPHeight+HOME_MODULE_HEIGHT*2;
                    
                    
                }else if (array.count == 6) {
                    TOPHeight = TOPHeight+HOME_MODULE_HEIGHT*3;
                    
                }
                
                TOPHeight = TOPHeight+10;
            }
                break;
                
            case 4: {
                NSArray *array = [dic objectForKey:@"subList"];
                
                if (array.count == 1 || array.count == 2) {
                    TOPHeight = TOPHeight + HOME_MODULE_HEIGHT;
                    
                }else if(array.count == 4) {
                    TOPHeight = TOPHeight + HOME_MODULE_HEIGHT*2;
                    
                }
                TOPHeight = TOPHeight+10;
                
            }
                break;
                
            case 5:{
                
                int template = [[dic objectForKey:@"template"] intValue];
                
                if (template == 201) {
                    TOPHeight = TOPHeight+HOME_MODULE_HEIGHT;
                }
                if  (template == 202 || template == 301 || template == 302 || template == 303|| template == 304 || template == 401) {
                    TOPHeight = TOPHeight+HOME_MODULE_HEIGHT*2;
                }
                if (template == 506 || template == 507 || template == 601) {
                    TOPHeight = TOPHeight+HOME_MODULE_HEIGHT*3;
                }
                TOPHeight = TOPHeight+10;
                
            }
                break;
                
            default:
                break;
        }
        
    }
    return TOPHeight;
    
    
}


@end
